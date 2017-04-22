package com.petbooking.Managers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Luciano José on 21/04/2017.
 */

public abstract class MaskManager {

    private static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "")
                .replaceAll("[(]", "").replaceAll("[)]", "");
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
            String myMask = mask;


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = MaskManager.unmask(s.toString());
                checkNineDigitForPhoneMask(start, str);
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : myMask.toCharArray()) {
                    if (m != '#' && str.length() > i) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void afterTextChanged(Editable s) {
            }

            public void checkNineDigitForPhoneMask(int start, String str) {
                if (myMask.startsWith("(##)")) {
                    myMask = start == 13 || str.length() == 11 ? "(##)#####.####" : "(##)####.####";
                }
            }

        };

    }
}
