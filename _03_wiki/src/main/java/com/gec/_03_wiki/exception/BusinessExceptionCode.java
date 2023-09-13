package com.gec._03_wiki.exception;

public enum BusinessExceptionCode {
    Select_Upload_Image("请选择要上传的图片"),
    Files_Larger_Than_10M("文件大小不能大于10M"),
    Files_Wrong_Format("请选择jpg,jpeg,gif,png格式的图片"),
    Save_File_Exception("保存文件异常");


    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
