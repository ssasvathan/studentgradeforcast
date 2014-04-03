<?php
 

 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['assessmentID']) && isset($_POST['name']) && isset($_POST['achiveGrade']) && isset($_POST['weight'])) {
 
    $assessmentID= $_POST['assessmentID'];
    $name = $_POST['name'];
	$achiveGrade = $_POST['achiveGrade'];
    $weight = $_POST['weight'];
	
    // include db connect class
    require_once dirname(__FILE__) . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
 
	// mysql update row with matched StudentID
    $result = mysql_query("UPDATE ASSESSMENT SET name = '$name', achiveGrade = '$achiveGrade', weight = '$weight' WHERE assessmentID = $assessmentID");

    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Assessment successfully updated.";
 
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