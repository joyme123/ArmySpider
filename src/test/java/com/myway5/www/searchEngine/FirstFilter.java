package com.myway5.www.searchEngine;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myway5.www.Spider.AbstFilterSpider;
import com.myway5.www.Util.Page;

public class FirstFilter extends AbstFilterSpider{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void filter(Object o) {
		Page page = (Page)o;
		Document document = page.getDocument();
		SocketClient client = new SocketClient("127.0.0.1",9999);

        byte[] head = new byte[4];
        head[0] = 0x01;
        String str = document.getElementsByTag("body").html();
        byte[] strByte = str.getBytes();
        Integer len = strByte.length;
        head[1] = (byte) ((byte)0xff & (len >> 16)) ;
        head[2] = (byte) ((byte)0xff & (len >> 8)) ;
        head[3] = (byte) ((byte)0xff & (len));
        byte[] total = new byte[4+len];

        System.out.println(total.length);

        for(int i = 0; i < len+4; i++){
            if(i < 4){
                total[i] = head[i];
            }else{
                total[i] = strByte[i-4];
            }
        }
        client.send(total);
	}

}
