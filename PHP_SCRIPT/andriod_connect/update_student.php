<?php
 
/*
 * Following code will update a student information
 * A product is identified by student id (StudentID)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['studentID']) && isset($_POST['firstName']) && isset($_POST['lastName']) && isset($_POST['userName']) && isset($_POST['password'])) {
 
    $studentID = $_POST['studentID'];
    $firstName = $_POST['firstName'];
    $lastName = $_POST['lastName'];
    $userName = $_POST['userName'];
	$password = $_POST['password'];
 
    // include db connect class
    require_once dirname(__FILE__) . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched StudentID
    $result = mysql_query("UPDATE student SET firstName = '$firstName', lastName = '$lastName', userName = '$userName', password = '$password' WHERE studentID = $studentID");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Student successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>