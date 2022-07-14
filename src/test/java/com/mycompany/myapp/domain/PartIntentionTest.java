package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartIntentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartIntention.class);
        PartIntention partIntention1 = new PartIntention();
        partIntention1.setId(1L);
        PartIntention partIntention2 = new PartIntention();
        partIntention2.setId(partIntention1.getId());
        assertThat(partIntention1).isEqualTo(partIntention2);
        partIntention2.setId(2L);
        assertThat(partIntention1).isNotEqualTo(partIntention2);
        partIntention1.setId(null);
        assertThat(partIntention1).isNotEqualTo(partIntention2);
    }
}
