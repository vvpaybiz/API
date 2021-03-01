<?php

$pay = new Bitake;
$pay->rmbPrice('CNY',695); //商户入金数字币报价（人民币）
//$pay -> pay('CNY',100);  //获取入金跳转链接
//$pay -> orderQuery('PAY1600328923');  //查询入金订单状态
//$pay -> withdrawal();   //申请法币出金
//$pay -> withdrawalHistory(); //法币出金历史记录
//$pay -> withdrawalDelete('WF1609222990622');  //删除未审核的法币出金申请记录




class Bitake{
    private $app_id = "41043343"; //商户帐号ID (APPID)<由 Bitake 分配,在app-API配置(pc商户后台--账户中心--商户信息) 中获取>
    private $mch_no = "9357"; //商户号 (UID)<由 Bitake 分配,在app-API配置(pc商户后台--账户中心--商户信息) 中获取>
    private $app_key ='73A8946D4ED1AD34ADA3F503601D3833';//密钥
    private $into_url = "http://merchant-api.bitake.io/api/recharge/check/v2";  //入金地址
	private $rmb_price_url = "http://merchant-api.bitake.io/api/recharge/convert/v1";//查询报价
    private $order_query_url = "http://merchant-api.bitake.io/api/recharge/order/query"; //查询订单状态
	private $withdrawal_url = "http://merchant-api.bitake.io/api/recharge/customer/withdrawal"; //法币出金申请
    private $withdrawal_history_url = "http://merchant-api.bitake.io/api/recharge/customer/withdrawal/history"; //法币入金申请历史
    private $withdrawal_delete_url = "http://merchant-api.bitake.io/api/recharge/customer/withdrawal/delete"; //删除未审核的法币出金申请记录

//curl -X GET -H "content-type:application/json" -H "access_key:8A42FADDD2397F4841B1F85C2C13094CAB152151" -H "app_id:41043343" http://merchant-api.bitake.io/api/recharge/convert/v1?timestamp=1609385362&p1=9357&p2=CNY&p3=695

    /**
     * 商户入金数字币报价（人民币）
     * @param $closeCurrency
     * @param $amount
     */
    public function rmbPrice($closeCurrency,$amount){
        //curl -X GET -H "content-type:application/json" -H "access_key:8A42FADDD2397F4841B1F85C2C13094CAB152151" -H "app_id:41043343" http://merchant-api.bitake.io/api/recharge/convert/v1?timestamp=1609385362&p1=9357&p2=CNY&p3=695
		$timestamp = time();
       $param = array(
           'p1' => $this->mch_no,
		   'p2' => $closeCurrency,
		   'p3' => $amount,
           'timestamp' => $timestamp
       );
	   $str = $this->mch_no.'&'.$timestamp;
        $url = $this->rmb_price_url;
        $url .= '?p1='.$this->mch_no.'&p2='.$closeCurrency.'&p3='.$amount.'&timestamp='.$param['timestamp'];
        $rlt = $this->httpGET($url,$str);
        var_dump($rlt);
        //{"code":200,"data":{"price":6.46},"message":"success"}

    }

    /**
     * 获取入金跳转链接
     * @param $closeCurrency
     * @param $amount
     */
    public function pay($closeCurrency,$amount){
		$orderNo = time().rand(1,100);
		$timestamp = time();
       $param = array(
           'p1' => $amount,
           'p2' => $this->mch_no,
           'p3' => $orderNo,
		   'p4' => $closeCurrency,
           'timestamp' => $timestamp
       );
	   $str = $amount.'&'.$this->mch_no.'&'.$orderNo.'&'.$timestamp;
        $url = $this->into_url;
        $url .= '?p1='.$param['p1'].'&p2='.$param['p2'].'&p3='.$param['p3'].'&timestamp='.$param['timestamp'];
        $rlt = $this->httpGET($url,$str);
        var_dump($rlt);
        //{"code":200,"data":{"url":"https://pay-api-ssl.bitake.io/mobile/buy?orderNo=USDT1609233285404590147&amount=100.0&usdtAmount=100.0&exchangeRate=1.0&merchantName=Bitake+Demo&closeCurrency=CNY"},"message":"success"}

    }

    /**
     * 查询入金订单状态
     * @param $merchantOrderNo
     */
    public function orderQuery($merchantOrderNo){
        $timestamp = time();

        $str = $this->mch_no.'&'.$timestamp;
        $url = $this->order_query_url;
        $url .= '?merchantNo='.$this->mch_no.'&merchantOrderNo='.$merchantOrderNo.'&timestamp='.$timestamp;
        $rlt = $this->httpGET($url,$str);
        var_dump($rlt);
        //{"code":200,"data":{"amount":"1000.0","orderNo":"USDT1600328924378686589","poundage":"34.0","sign":"B00664E84B184770B49630F1D1FD6C0F0E0E805E","currency":"USDT(ERC20)","state":"1","merchantOrderNo":"PAY1600328923","merchantNo":"5066","timestamp":"1609233192"},"message":"success"}

    }

    /** 申请法币出金
     * @param $closeCurrency
     * @param $amount
     */
    public function withdrawal(){
        $timestamp = time();
        $jsonStr = json_encode(array(
            'merchantNo' => $this->mch_no,//商户号
            'fiatCurrency' => "CNY",   //出金法币类型
            'amount' => 1000.0,  //出金法币数量
            'customerId' => "110", //用户标识 用于查询历史的查询条件
            'accountName' => "测试", //银行账户姓名
            'bankNumber' => "535224124242",//银行卡号
            'account' => "345345245",           //银行账号
            'bankName' => "中国银行",         //银行名称
            'subbranch' => "北京西二旗分行",             //支行
            'iBank' => "344244",               //IBANK
            'bankAddress' => "",             //银行地址
            'swiftCode' => "",             //Swift Code
            'bankCode' => "",               //银行代码
            'remarks' => "",                //备注
            'type' => "1"                  //类型  1 正常  2 加急 
        ));

        $str = $timestamp;
        $url = $this->withdrawal_url;
        $url .= '?timestamp='.$timestamp;
        $rlt = $this->http_post_json($url,$jsonStr,$str);
        var_dump($rlt);
        //{"code":200,"message":"success"}

    }

    /**
     * 法币出金历史记录
     */
    public function withdrawalHistory(){
        $timestamp = time();
        $str = $this->mch_no.'&'.$timestamp;
        $url = $this->withdrawal_history_url;
        $url .= '?merchantNo='.$this->mch_no
            .'&timestamp='.$timestamp
            .'&currPage=1'
            .'&pageSize=20'
            .'&customerId=110'
            .'&fiatCurrency=CNY';
        $rlt = $this->httpGET($url,$str);
        var_dump($rlt);
        //{"total":2,"code":200,"data":[{"account":"345345245","accountName":"测试","amount":1000.0,"bankAddress":"","bankCode":"","bankName":"中国银行","bankNumber":"535224124242","comeFrom":2,"createTime":"2020-12-29 17:10:42","currencyCount":null,"currencyTypeId":1,"customerId":"110","fee":null,"fiatCurrency":"CNY","financialReviewTime":null,"iBank":"344244","id":7,"investorPassword":null,"merchantNo":null,"orderNo":"WF1609233042438","refuseReason":null,"remarks":"","state":0,"subbranch":"北京西二旗分行","swiftCode":"","traderId":null,"traderReviewTime":null,"type":1,"userId":535,"withdrawalRate":null},{"account":null,"accountName":"接口测试","amount":1000.0,"bankAddress":null,"bankCode":null,"bankName":null,"bankNumber":null,"comeFrom":2,"createTime":"2020-12-28 14:57:15","currencyCount":1000.0,"currencyTypeId":1,"customerId":null,"fee":0.0,"fiatCurrency":"CNY","financialReviewTime":"2020-12-28 16:04:49","iBank":null,"id":5,"investorPassword":null,"merchantNo":null,"orderNo":"WF1609138635895","refuseReason":null,"remarks":null,"state":1,"subbranch":null,"swiftCode":null,"traderId":1,"traderReviewTime":null,"type":1,"userId":535,"withdrawalRate":1.0}],"pageSize":20,"message":"success","currentPage":1}

    }

    /**
     * 删除未审核的法币出金订单
     * @param $withdrawalOrderNo
     */
    public function withdrawalDelete($withdrawalOrderNo){
        $timestamp = time();
        $str = $this->mch_no.'&'.$timestamp;
        $url = $this->withdrawal_delete_url;
        $url .= '?merchantNo='.$this->mch_no
            .'&timestamp='.$timestamp
            .'&orderNo='.$withdrawalOrderNo;  //出金订单号
        $rlt = $this->httpGET($url,$str);
        var_dump($rlt);
        //{"code":200,"message":"success"}

    }



    /**  PHP 的 HMAC_SHA1算法 实现
     * @param $str
     * @param $key
     * @return string
     */
    function getSignature($str, $key) {
        $signature = "";
        if (function_exists('hash_hmac')) {
            $signature = bin2hex(hash_hmac("sha1", $str, $key, true));
        } else {
            $blocksize = 64;
            $hashfunc = 'sha1';
            if (strlen($key) > $blocksize) {
                $key = pack('H*', $hashfunc($key));
            }
            $key = str_pad($key, $blocksize, chr(0x00));
            $ipad = str_repeat(chr(0x36), $blocksize);
            $opad = str_repeat(chr(0x5c), $blocksize);
            $hmac = pack(
                'H*', $hashfunc(
                    ($key ^ $opad) . pack(
                        'H*', $hashfunc(
                            ($key ^ $ipad) . $str
                        )
                    )
                )
            );
            $signature = bin2hex($hmac);
        }
        return $signature;
    }
    /**
     * 远程获取数据，GET模式
     */
    function httpGET($url,$str) {

        echo 'url:'.$url."</br>";    //请求地址和参数：http://merchant-api.bitake.io/api/recharge/check/v2?p1=100&p2=11082429&p3=1553155910&timestamp=1553155910

        echo 'str:'.$str."</br>";   //加密前字符串：100&11082429&1553155910&1553155910
        if(empty($str)){
            return false;
        }
        $sign = strtoupper($this->getSignature($str,$this->app_key));  //加密后(大写)：5F51F8065B325EC3491526612CB2A47B84E5E10B
        echo 'sign:'.$sign."</br>";
        $headers = array(
            'content-type:application/json',
            'access_key:'.$sign,
            'app_id:'.$this->app_id
        );
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL,$url);
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, 'GET');  
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($curl, CURLOPT_HTTPHEADER, $headers); 
        //执行命令
        $data = curl_exec($curl);
        //关闭URL请求
        curl_close($curl);
        return $data;
    }

    function http_post_json($url, $jsonStr,$str){
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $jsonStr);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $sign = strtoupper($this->getSignature($str,$this->app_key));  //加密后(大写)：5F51F8065B325EC3491526612CB2A47B84E5E10B
        echo 'sign:'.$sign."</br>";
        curl_setopt($ch, CURLOPT_HTTPHEADER, array(
                'Content-Type: application/json; charset=utf-8',
                'Content-Length: ' . strlen($jsonStr),
                'access_key:'.$sign,
                'app_id:'.$this->app_id
            )
        );
        $response = curl_exec($ch);
        curl_close($ch);

        return $response;
    }


}