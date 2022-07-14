package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BomIntentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BomIntention.class);
        BomIntention bomIntention1 = new BomIntention();
        bomIntention1.setId(1L);
        BomIntention bomIntention2 = new BomIntention();
        bomIntention2.setId(bomIntention1.getId());
        assertThat(bomIntention1).isEqualTo(bomIntention2);
        bomIntention2.setId(2L);
        assertThat(bomIntention1).isNotEqualTo(bomIntention2);
        bomIntention1.setId(null);
        assertThat(bomIntention1).isNotEqualTo(bomIntention2);
    }
}
