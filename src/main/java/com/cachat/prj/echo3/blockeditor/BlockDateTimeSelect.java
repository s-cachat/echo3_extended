package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;

/**
 *
 * @author scachat
 */
public class BlockDateTimeSelect extends BlockDateSelect {

    public BlockDateTimeSelect(BlockField bf) {
        super(bf);
    }

    public BlockDateTimeSelect(LocalisedItem li, String property) {
        super(li, property, true);
    }

    public BlockDateTimeSelect(LocalisedItem li, String property, boolean withNull) {
        super(li, property, true, withNull);
    }

}
