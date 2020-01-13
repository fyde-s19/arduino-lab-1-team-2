#!/bin/bash

TA_MODE=false

PREPARING=false
CLEANING=false

PYTHONEXEC=python2
if [ "$(uname)" == "Darwin" ]; then
    PYTHONEXEC=python       
fi

function try() { [[ $- = *e* ]]; SAVED_OPT_E=$?;set +e;}
function throw() { exit $1;}
function catch() { export ex_code=$?;(( $SAVED_OPT_E )) && set +e;return $ex_code;}
function enable_throwing_errors() { set -e;}
function disable_throwing_errors() { set +e;}

main_dir="$(pwd)"
submissions_dir="$main_dir/submissions"
grading_project_original="$main_dir/grading_project_original"
grading_project="$main_dir/grading_project"
detailed_feedback_dir="$main_dir/detailed_feedback"
brief_feedback_file="$main_dir/brief_results.csv"

collect_results()
{
	mkdir -p "$detailed_feedback_dir"
	cd "$submissions_dir"
	for tested_submission in */
	do
		tested_submission="${tested_submission:0:${#tested_submission}-1}"
        if [ -f "$tested_submission/test_log.txt" ]
        then
		    brief_result_line=$( $PYTHONEXEC "$main_dir/helper_scripts/score_extractor.py" "$tested_submission/test_log.txt" "$tested_submission" )
		    echo "$brief_result_line" >> "$brief_feedback_file"
        fi
        
		zip -ry9Tm "$tested_submission" "$tested_submission" > /dev/null
		mv "$tested_submission".zip "$detailed_feedback_dir"

	done
	cd "$main_dir"
}

perform_test_on_this_submission()
{
    enable_throwing_errors

	this_submission_dir="$(pwd)"
	src_dir="$(find . -name src)"

	rm -rf "$grading_project/src/main/java/assignment4"
	mv "$src_dir/assignment4" "$grading_project/src/main/java/"
    cp "$grading_project_original/src/main/java/assignment4/"*.java "$grading_project/src/main/java/assignment4/"

	cd "$grading_project"
    if [ "$TA_MODE" = true ]
    then
	    mvn clean test > test_log.txt 2>&1 || true
    else
	    mvn clean test 2>&1 | tee test_log.txt || true
    fi

	cd "$this_submission_dir"

	rm -rf *

	mv "$grading_project/test_log.txt" .

    if ls "$grading_project/target/surefire-reports/"*.xml 1> /dev/null 2>&1; then
        mv "$grading_project/target/surefire-reports/"*.xml .
    fi
}

run_test_on_submissions()
{
    enable_throwing_errors

	cd "$submissions_dir"

    if ( ls *.zip 1> /dev/null 2>&1 )
    then
	    for submission_raw_name in *.zip
	    do
	    	submission_name="${submission_raw_name%%.*}"
            try
            (
	    	    echo "### going on: " "$submission_name"

	    	    mkdir "$submission_name"
	    	    cd "$submission_name"
	    	    unzip ../"$submission_raw_name" > /dev/null
	    	    find . -name __MACOSX -exec rm -rfv {} \; > /dev/null 2>&1 || echo ""
	    	    rm ../"$submission_raw_name"

	    	    perform_test_on_this_submission

	    	    cd "$submissions_dir"
            )
            catch ||
            {
                echo "###### submission format error on $submission_name"
                echo "\"$submission_name\", 0, 0, 0, 0" >> "$brief_feedback_file"
                cd "$submissions_dir"
            }
	    done
    fi

	cd "$main_dir"
}

prepare_maven_idempotent()
{
    if [ -f ~/.profile ]
    then
        . ~/.profile
    fi

    if ! type "mvn" > /dev/null 2>&1 # if maven is not loaded
    then
        module load maven
    fi
}

grade()
{
    enable_throwing_errors

    rm -rf "$grading_project"
    cp -r "$grading_project_original" "$grading_project"

    prepare_maven_idempotent
	run_test_on_submissions
	echo "collecting results ..."
	collect_results
	echo "finished"

    disable_throwing_errors
}

clean()
{
    rm -rf "$grading_project" "$grading_project_original" "$brief_feedback_file" "$detailed_feedback_dir"
    rm -rf submissions
    git checkout -- submissions
}

prepare()
{
	if [ ! -f "$brief_feedback_file" ]
	then
		echo '"submission_name", "total", "fail", "error", "skipped"' > "$brief_feedback_file"
	fi

    local solution_dir="../solution/grading_structure_project/"
    local data_dir="src/test/data/cli_integration_inouts"
    local src_dir="src/main/java/assignment4"

    cp -R "$solution_dir" "$grading_project_original"

    rm -f "$solution_dir/$src_dir/Critter.java" "$solution_dir/$src_dir/Header.java" "$solution_dir/$src_dir/"*Test.java
    rm -f "$solution_dir/$src_dir/Main.java"

    if [[ $TA_MODE == true ]]; then
        rm -f "$grading_project_original/src/test/java/assignment4/A4SampleTest"*".java"
    else
        rm -f "$grading_project_original/src/test/java/assignment4/TestStage"*".java"
        rm -f "$grading_project_original/src/test/java/assignment4/A4InterpreterTest.java"


        rm -rf "$grading_project_original/$data_dir"/*
        cp -R "$solution_dir/$data_dir/empty_world"         "$grading_project_original/$data_dir/"
        cp -R "$solution_dir/$data_dir/error_processing"    "$grading_project_original/$data_dir/"
        cp -R "$solution_dir/$data_dir/kill_all_critter"    "$grading_project_original/$data_dir/"
        cp -R "$solution_dir/$data_dir/create_large_critter"  "$grading_project_original/$data_dir/"
    fi
}

main()
{
    if [[ $CLEANING == true ]]; then
        clean
    elif [[ $PREPARING == true ]]; then
        prepare
    else
        grade
    fi
}

while [[ $# > 0 ]]
do
    arg="$1"
    case $arg in
        --ta)       TA_MODE=true;;
        --clean)    CLEANING=true;;
        --prepare)  PREPARING=true;;
    esac

    shift
done

main
