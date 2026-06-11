package com.kuafor.ui.components;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.kuafor.ui.theme.AppTheme;

import javax.swing.*;

public class DatePickerField extends DateTimePicker {

    public DatePickerField() {
        getDatePicker().setPreferredSize(new java.awt.Dimension(160, 36));
        getTimePicker().setPreferredSize(new java.awt.Dimension(100, 36));
        getDatePicker().getComponentDateTextField().setFont(AppTheme.FONT_INPUT);
        getTimePicker().getComponentTimeTextField().setFont(AppTheme.FONT_INPUT);
    }
}
