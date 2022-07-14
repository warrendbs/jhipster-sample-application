package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BomChildTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BomChild.class);
        BomChild bomChild1 = new BomChild();
        bomChild1.setId(1L);
        BomChild bomChild2 = new BomChild();
        bomChild2.setId(bomChild1.getId());
        assertThat(bomChild1).isEqualTo(bomChild2);
        bomChild2.setId(2L);
        assertThat(bomChild1).isNotEqualTo(bomChild2);
        bomChild1.setId(null);
        assertThat(bomChild1).isNotEqualTo(bomChild2);
    }
}
