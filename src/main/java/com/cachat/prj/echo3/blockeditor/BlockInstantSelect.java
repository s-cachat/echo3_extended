package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;

/**
 *
 * @author scachat
 */
public class BlockInstantSelect extends BlockInstantDateSelect {

    public BlockInstantSelect(BlockField bf) {
        super(bf);
    }

    public BlockInstantSelect(LocalisedItem li, String property) {
        super(li, property, true);
    }

    public BlockInstantSelect(LocalisedItem li, String property, boolean withNull) {
        super(li, property, true, withNull);
    }

}
