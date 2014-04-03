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
if (isset($_GET["studentID"])) {
    $studentID = $_GET['studentID'];
 
    // get courses by student ID from COURSE table
    $result = mysql_query("SELECT studentcourse.courseID, course.courseName FROM studentcourse JOIN course ON (studentcourse.courseID = course.courseID) WHERE studentcourse.studentID = '$studentID'");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
			$response["courses"] = array();
			
            while ($row = mysql_fetch_array($result)) {
			
            $course = array();
            $course["courseID"] = $row["courseID"];
            $course["courseName"] = $row["courseName"];
 
			// push single assessment into final response array
			array_push($response["courses"], $course);
			}

            // success
            $response["success"] = 1;
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no student found
            $response["success"] = 0;
            $response["message"] = "No courses found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no student found
        $response["success"] = 0;
        $response["message"] = "No courses found";
 
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