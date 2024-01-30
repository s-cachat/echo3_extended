package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.util.BeanTools;
import java.io.File;
import java.util.List;
import jakarta.validation.Validator;

/**
 * un bloc affichant un bouton de téléchargement de fichier. La propriété doit
 * être le chemin du fichier.
 *
 * @author scachat
 */
public class BlockDownloadFile extends BlockField<ButtonEx> {

    /**
     * Le type de contenu (mime)
     */
    private String contentType;
    /**
     * if true, the full line will be hidden if the path is null or can't be
     * read
     */
    private boolean hideIfNull;
    /**
     * le fichier à télécharger
     */
    private File file;

    public BlockDownloadFile(BlockField x) {
        super(x);
        editor = new ButtonEx(localisedItem.getBaseString("download"));
        editor.setStyleName("Button");
        contentType = ((BlockDownloadFile) x).contentType;
        hideIfNull = ((BlockDownloadFile) x).hideIfNull;
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property le nom de la propriete
     * @param hideIfNull if true, the full line will be hidden if the value is
     * @param contentType Le type de contenu (mime) null
     */
    public BlockDownloadFile(BaseApp li, String property, String contentType, boolean hideIfNull) {
        super(li, property);
        editor = new ButtonEx(localisedItem.getBaseString("download"));
        editor.setStyleName("Button");
        editor.addActionListener((e) -> download());
        this.contentType = contentType;
        this.hideIfNull = hideIfNull;
    }

    @Override
    public void copyObjectToUi() {
        if (hideIfNull) {
            String path = BeanTools.get(getParent().getCurrent(), property);
            file = path == null ? null : new File(path);
            if (file == null || !file.canRead()) {
                editor.setVisible(false);
                label.setVisible(false);
                error.setVisible(false);
                return;
            } else {
                editor.setVisible(true);
                label.setVisible(true);
                error.setVisible(true);
            }
        }
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        return false;
    }

    /**
     * ajouter un message d'erreur sur un champ
     *
     * @param pp le chemin de la propriété
     * @param msg le message
     * @return true si le message a pu être ajouté, false sinon
     */
    @Override
    public boolean appendError(String pp, String msg) {
        return false;
    }

    public void download() {
        if (file != null && file.canRead()) {
            ((BaseApp) localisedItem).sendDoc(contentType, file.getName(), file);
        }
    }
}
