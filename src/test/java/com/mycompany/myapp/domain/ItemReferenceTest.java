package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemReferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemReference.class);
        ItemReference itemReference1 = new ItemReference();
        itemReference1.setId(1L);
        ItemReference itemReference2 = new ItemReference();
        itemReference2.setId(itemReference1.getId());
        assertThat(itemReference1).isEqualTo(itemReference2);
        itemReference2.setId(2L);
        assertThat(itemReference1).isNotEqualTo(itemReference2);
        itemReference1.setId(null);
        assertThat(itemReference1).isNotEqualTo(itemReference2);
    }
}
