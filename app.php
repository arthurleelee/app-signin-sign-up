<?php
$link = mysqli_connect("127.0.0.1","test","123456","app");
mysqli_query($link,"set names utf8");
$email=$_POST['email'];
$name=$_POST['name'];
$phone=$_POST['phone'];
$pwd=$_POST['pwd'];
$q=mysqli_query($link,"SELECT * from user where (Email='$email' OR Name='$name' OR Phone='$phone') AND Pwd='$pwd' ");
//$q=mysqli_query($link,"SELECT * from user where Email='test@gmail.com' AND Pwd='123456' ");
while($e=mysqli_fetch_assoc($q))
    $output[]=$e;
    //$a = 1;
print(json_encode($output));
//echo($a);
mysqli_close($link);
?>