package com.ds.component.locker.constants;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author rockjing
 * @version 1.0
 */

public enum LockResult {


    /**
     * 不存在该KEY
     */
    NOT_EXIST(2L,"不存在该KEY"),

    /**
     * 操作成功
     */
    OPERATION_OK(1L,"操作成功"),

    /**
     * 操作成功
     */
    OPERATION_NOK(0L,"操作失败"),


    /**
     * 不知道的返回结果
     */
    UNKNOWN(999L,"不知道的返回结果，请查看对应的LUA脚本");

    private String description;

    private Long resultCode;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getResultCode() {
        return resultCode;
    }

    public void setResultCode(Long resultCode) {
        this.resultCode = resultCode;
    }

    LockResult(Long resultCode, String description){
        this.resultCode = resultCode;
        this.description = description;
    }



    public static LockResult getResultByResultCode(Object resultCode){

        Optional<LockResult> lockerResultType =
                Arrays.stream( LockResult.values()).filter(x->x.getResultCode().equals(resultCode)).findFirst();
        return lockerResultType.orElse(LockResult.UNKNOWN);


    }

}
