package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.validation.Validator;

/**
 * champs texte
 *
 * @author scachat
 */
public class BlockTimeAsInt extends BlockTextField {

    public BlockTimeAsInt(BlockField x) {
        super(x);
        adjustField();
    }

    public BlockTimeAsInt(LocalisedItem li, String property) {
        super(li, property);
        adjustField();
    }

    @Override
    public void copyObjectToUi() {
        int n = ((Number) BeanTools.getRaw(getParent().getCurrent(), property)).intValue();
        int ms = n % 1000;
        n = (n - ms) / 1000;
        int s = n % 60;
        n = (n - s) / 60;
        int m = n % 60;
        n = (n - m) / 60;
        int h = n % 24;
        editor.setText(String.format("%d:%02d:%02d", h, m, s));
    }
    Pattern pat = Pattern.compile("(\\d+)([-:/](\\d+)([-:/](\\d+))?)?");

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            Matcher mat = pat.matcher(editor.getText());
            if (mat.matches()) {
                int n = Integer.parseInt(mat.group(1));
                n = n * 60;
                if (mat.group(3) != null) {
                    n = n + Integer.parseInt(mat.group(3));
                }
                n = n * 60;
                if (mat.group(5) != null) {
                    n = n + Integer.parseInt(mat.group(5));
                }

                BeanTools.setRaw(getParent().getCurrent(), property, n * 1000);
            } else {
                error.setText("Err.");
                return true;
            }
            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }

    private void adjustField() {
        editor.setRegex("\\d?\\d:\\d\\d");
    }
}
