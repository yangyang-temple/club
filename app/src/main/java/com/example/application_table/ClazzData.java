package com.example.application_table;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * 选择班级
 */
public class ClazzData implements IPickerViewData {

    private String name;
    private List<MajorBean> major;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MajorBean> getMajor() {
        return major;
    }

    public void setMajor(List<MajorBean> major) {
        this.major = major;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    public static class MajorBean {

        private String name;
        private List<String> clazz;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getClazz() {
            return clazz;
        }

        public void setClazz(List<String> clazz) {
            this.clazz = clazz;
        }

    }

}

