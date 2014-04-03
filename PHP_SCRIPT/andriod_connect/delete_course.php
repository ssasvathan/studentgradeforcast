<?php
 

 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['courseID']) && isset($_POST['studentID']) ) {
 
    $courseID= $_POST['courseID'];
	$studentID= $_POST['studentID'];
 
    // include db connect class
    require_once dirname(__FILE__) . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched StudentID
    $result = mysql_query("DELETE FROM studentcourse WHERE studentID = $studentID AND courseID = $courseID");
	$result2 = mysql_query("DELETE FROM ASSESSMENT WHERE studentID = $studentID AND courseID = $courseID ");
 
    // check if row inserted or not
    if ($result && $result2) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Course successfully deleted.";
 
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