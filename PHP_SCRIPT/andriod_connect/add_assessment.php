<?php
 
/*
 * Following code will create a new student row
 * All student details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['studentID']) && isset($_POST['courseID']) && isset($_POST['name']) && isset($_POST['weight']) && isset($_POST['achiveGrade'])) {
 
    $studentID = $_POST['studentID'];
    $courseID = $_POST['courseID'];
	$name = $_POST['name'];
	$achiveGrade = $_POST['achiveGrade'];
	$weight = $_POST['weight'];

 
    // include db connect class
    require_once dirname(__FILE__) . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO ASSESSMENT (name, weight, achiveGrade, courseID, studentID) VALUES ('$name', '$weight', '$achiveGrade', '$courseID', '$studentID')");
	 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Assessment successfully added.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
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