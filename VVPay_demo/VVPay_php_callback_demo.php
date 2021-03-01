<?php
//仅用于测试，请更改为您的帐户信息

$data = [

    'amount'=>$_POST["amount"],
    'currency'=>$_POST["currency"],
    'exchangeRate'=>$_POST["exchangeRate"],
    'poundage'=>$_POST['poundage'],
    'merchantNo'=>$_POST['merchantNo'],
    'merchantOrderNo'=>$_POST['merchantOrderNo'],
    'orderNo'=>$_POST['orderNo'],
    'timestamp'=>$_POST['timestamp'],
    'sign'=>$_POST['sign']

];


$str = var_export($data,TRUE);

//追加写入
 file_put_contents(__DIR__."/log.txt", date('Y-m-d H:i:s'). $str.PHP_EOL, FILE_APPEND);



$order_num=$data['orderNo'];

    $con = mysqli_connect('localhost','','','');
    if (!$con)
    {
        die('Could not connect: ' . mysqli_error($con));
    }
    
    mysqli_select_db($con,"con_order");
 
    mysqli_set_charset($con, "utf8");
 
    $sql = "select id,NUMBER,LOGIN,USD,CNY,sign,order_num from con_order where order_num='$order_num'";
    $result = mysqli_query($con,$sql);
 
    while($row = mysqli_fetch_array($result))
    {
        
    $NUMBER=$row['NUMBER'];
    $LOGIN=$row['LOGIN'];
    $USD=$row['USD'];
    $CNY=$row['CNY'];
    $sersign=$row['sign'];
    $ISPAY="1";
        
    }
 
    mysqli_close($con);




$arrs = array('NUMBER' => $NUMBER, 'LOGIN' => $LOGIN, 'USD' => $USD, 'CNY' => $CNY, 'ISPAY' => $ISPAY); //生成回调数组

$arrsjson = json_encode($arrs); //回调数组转换为JSON字符串
$arrjm = base64_encode($arrsjson); //回调数组64位加密
$json = json_encode(['data' => $arrjm, 'sign' => $sersign]); //数组+sign一起加密

//追加写入
file_put_contents(__DIR__."/log.txt", date('Y-m-d H:i:s'). $arrsjson.PHP_EOL, FILE_APPEND);

//if($sign !== $_POST['sign'] || $_POST['status'] !== 'success') die('fail');
 
$result = _http_request('这里填自动入金地址',array('callback' => base64_encode($json)));

file_put_contents(__DIR__."/log.txt", date('Y-m-d H:i:s'). $result .PHP_EOL, FILE_APPEND);

//HTTP请求（支持HTTP/HTTPS，支持GET/POST）
function _http_request($url, $data = null)
{
    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, $url);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE);
    curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, FALSE);
    if (!empty($data)){
        curl_setopt($curl, CURLOPT_POST, 1);
        curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
    }
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
    $output = curl_exec($curl);
    curl_close($curl);
    return $output;
}


echo json_encode(array('code' => 200));

?>
