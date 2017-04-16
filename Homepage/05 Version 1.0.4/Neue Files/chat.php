<?php
	$from = $_COOKIE["benutzer"];
	$to = $_COOKIE["empfaenger"];
	$message = $_POST["nachricht"];
	$pfad = "chats/" . $from . "/" . $to;	
	if(file_exists($pfad)){
		$SenderDatei = "chats/" . $from . "/" . $to . "/chat.txt";
		$EmpfaengerDatei = "chats/" . $to . "/" . $from . "/chat.txt";
		file_put_contents ($SenderDatei, $from . " schrieb um " . date("h:i:sa") . ": " . $message . "<hr>\r\n",FILE_APPEND);
		file_put_contents ($EmpfaengerDatei, $from . " schrieb um " . date("h:i:sa") . ": " . $message . "<hr>\r\n",FILE_APPEND);
	}else{
		mkdir ("chats/" . $from . "/" . $to);
		chmod("chats/" . $from . "/" . $to,0777);
		mkdir ("chats/" . $to . "/" . $from); 
		chmod("chats/" . $to . "/" . $from,0777);
		fopen ("chats/" . $from . "/" . $to . "/chat.txt", "w");
		fopen ("chats/" . $to . "/" . $from . "/chat.txt", "w");
		$SenderDatei = "chats/" . $from . "/" . $to . "/chat.txt";
		$EmpfaengerDatei = "chats/" . $to . "/" . $from . "/chat.txt";
		file_put_contents ($SenderDatei, $from . " schrieb um " . date("h:i:sa") . ": " . $message . "<hr>\r\n",FILE_APPEND);
		file_put_contents ($EmpfaengerDatei, $from . " schrieb um " . date("h:i:sa") . ": " . $message . "<hr>\r\n",FILE_APPEND);
	}
	header("location: messenger.php");
?>