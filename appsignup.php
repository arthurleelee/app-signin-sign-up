<?php
$link = mysqli_connect("127.0.0.1","test","123456","app");
mysqli_query($link,"set names utf8");
$email=$_POST['email'];
$name=$_POST['name'];
$phone=$_POST['phone'];
$pwd=$_POST['pwd'];
$q0=mysqli_query($link,"SELECT * from user where Email='$email'");
$q1=mysqli_num_rows($q0);
if($q1 < 1 && $email !="" && $pwd !=""){
    if($phone == "" && $name == ""){
        mysqli_query($link,"INSERT INTO user (Email,Name,Pwd,Phone,Personalimg) VALUES ('$email', NULL, '$pwd', NULL, NULL)");
    }else if($phone != "" && $name == ""){
        mysqli_query($link,"INSERT INTO user (Email,Name,Pwd,Phone,Personalimg) VALUES ('$email', NULL, '$pwd', '$phone', NULL)");
    }else if($phone == "" && $name != ""){
        mysqli_query($link,"INSERT INTO user (Email,Name,Pwd,Phone,Personalimg) VALUES ('$email', '$name', '$pwd', NULL, NULL)");
    }else{
        mysqli_query($link,"INSERT INTO user (Email,Name,Pwd,Phone,Personalimg) VALUES ('$email', '$name', '$pwd', '$phone', NULL)");
    }
    $q=mysqli_query($link,"SELECT * from user where Email='$email' ");
    while($e=mysqli_fetch_assoc($q))
        $output[]=$e;
    print(json_encode($output));
}else{
    $a = "0";
    echo($a);
}
mysqli_close($link);
?>