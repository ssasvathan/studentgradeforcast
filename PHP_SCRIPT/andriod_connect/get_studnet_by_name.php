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

if(isset($_GET["userName"]))
{
	$userName = $_GET['userName'];
}else
{
	$studentID = $_GET['studentID'];
}
 
// check for post data
if ( !empty($userName) || !empty($studentID) ) {   
 
    // get a student from students table
	if(empty($studentID))
	{
		$result = mysql_query("SELECT *FROM student WHERE userName = '$userName'");
	}else
	{
		$result = mysql_query("SELECT *FROM student WHERE studentID = $studentID");
	}
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $student = array();
            $student["studentID"] = $result["studentID"];
            $student["firstName"] = $result["firstName"];
            $student["lastName"] = $result["lastName"];
            $student["userName"] = $result["userName"];
            $student["password"] = $result["password"];

            // success
            $response["success"] = 1;
 
            // user node
            $response["student"] = array();
 
            array_push($response["student"], $student);
 
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