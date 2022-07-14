package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BomSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BomSource.class);
        BomSource bomSource1 = new BomSource();
        bomSource1.setId(1L);
        BomSource bomSource2 = new BomSource();
        bomSource2.setId(bomSource1.getId());
        assertThat(bomSource1).isEqualTo(bomSource2);
        bomSource2.setId(2L);
        assertThat(bomSource1).isNotEqualTo(bomSource2);
        bomSource1.setId(null);
        assertThat(bomSource1).isNotEqualTo(bomSource2);
    }
}
