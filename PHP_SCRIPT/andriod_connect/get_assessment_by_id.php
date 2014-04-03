<?php
 

 
// array for JSON response
$response = array();
 
// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["assessmentID"])) {
    $assessmentID = $_GET['assessmentID'];
 
    // get a student from students table
    $result = mysql_query("SELECT assessmentID, name, achiveGrade, weight FROM ASSESSMENT WHERE assessmentID = '$assessmentID'");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $assessment = array();
            $assessment["assessmentID"] = $result["assessmentID"];
            $assessment["name"] = $result["name"];
            $assessment["achiveGrade"] = $result["achiveGrade"];
            $assessment["weight"] = $result["weight"];
        
            // success
            $response["success"] = 1;
 
            // user node
            $response["assessment"] = array();
 
            array_push($response["assessment"], $assessment);
 
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