package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartSource.class);
        PartSource partSource1 = new PartSource();
        partSource1.setId(1L);
        PartSource partSource2 = new PartSource();
        partSource2.setId(partSource1.getId());
        assertThat(partSource1).isEqualTo(partSource2);
        partSource2.setId(2L);
        assertThat(partSource1).isNotEqualTo(partSource2);
        partSource1.setId(null);
        assertThat(partSource1).isNotEqualTo(partSource2);
    }
}
