package com.yxhl.platform.common.web.editor;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * 字符串转换，防止xss攻击
 */
@Component
public class StringEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }
    
}
