<?php
 
/*
 * Following code will get single student details
 * A student is identified by student id (studnetID)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["studentID"]) && isset($_GET['courseID'])) {
    $studentID = $_GET['studentID'];
	$courseID = $_GET['courseID'];
 
    // get a student from ASSESMENT table
    $result = mysql_query("SELECT *FROM ASSESSMENT WHERE studentID = '$studentID' AND courseID = '$courseID' ");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
			$response["assessments"] = array();
			
            while ($row = mysql_fetch_array($result)) {
			
            $assessment = array();
            $assessment["assessmentID"] = $row["assessmentID"];
            $assessment["name"] = $row["name"];
            $assessment["weight"] = $row["weight"];
            $assessment["achiveGrade"] = $row["achiveGrade"];
            $assessment["courseID"] = $row["courseID"];
			$assessment["studentID"] = $row["studentID"];
			
			// push single assessment into final response array
			array_push($response["assessments"], $assessment);
			}

            // success
            $response["success"] = 1;
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no student found
            $response["success"] = 0;
            $response["message"] = "No student found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no student found
        $response["success"] = 0;
        $response["message"] = "No student found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>