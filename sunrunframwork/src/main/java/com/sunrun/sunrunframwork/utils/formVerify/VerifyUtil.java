package com.sunrun.sunrunframwork.utils.formVerify;

import android.widget.EditText;

import java.util.List;
import java.util.Map;

/**
 * 有效性效验工具
 * Created by WQ on 2017/1/24.
 */
public class VerifyUtil {
    public static void verify(EditText editText, IVerifyer...iVerifyers) throws FormatException {
        String text=editText.getText().toString();
        verify(text,iVerifyers);

    }
    public static void verify(String text , IVerifyer...iVerifyers) throws FormatException {
        for (IVerifyer verifyer : iVerifyers) {
            if(verifyer!=null) {
                verifyer.verify(text);
            }
        }
    }
    public static void emptyVerify(List data,String tiptxt) throws FormatException {
        if(data==null || data.size()==0){
            throw  new FormatException(tiptxt);
        }
    }
    public static void emptyVerify(Map data, String tiptxt) throws FormatException {
        if(data==null || data.isEmpty()){
            throw  new FormatException(tiptxt);
        }
    }
    public static void verify( IRulerVeriyer iRulerVeriyer,EditText...editTexts) throws FormatException {
        String texts[]=new String[editTexts.length];
        for (int i = 0; i < texts.length; i++) {
            texts[i]=editTexts[i].getText().toString();
        }
        iRulerVeriyer.verify(texts);
    }

    /**
     * 效验字符内容格式
     * @param text 待效验字符串
     * @param verifyNames 效验器名称
     * @param tipTexts    效验失败的提示信息,为空时使用默认
     * @throws FormatException
     */
    public static void verify(String text,String[] verifyNames,String[] tipTexts) throws FormatException {
        IVerifyer iVerifys[]=new IVerifyer[verifyNames.length];
        for (int i = 0; i < verifyNames.length; i++) {
            String verifyName=verifyNames[i];
            String tipText=i<tipTexts.length?tipTexts[i]:null;
            iVerifys[i]=VerifyerSet.getVerifyer(verifyName,tipText);
        }
        verify(text,iVerifys);
    }
}
