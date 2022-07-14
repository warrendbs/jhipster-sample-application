package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bom.class);
        Bom bom1 = new Bom();
        bom1.setId(1L);
        Bom bom2 = new Bom();
        bom2.setId(bom1.getId());
        assertThat(bom1).isEqualTo(bom2);
        bom2.setId(2L);
        assertThat(bom1).isNotEqualTo(bom2);
        bom1.setId(null);
        assertThat(bom1).isNotEqualTo(bom2);
    }
}
