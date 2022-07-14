package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentIntentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentIntention.class);
        DocumentIntention documentIntention1 = new DocumentIntention();
        documentIntention1.setId(1L);
        DocumentIntention documentIntention2 = new DocumentIntention();
        documentIntention2.setId(documentIntention1.getId());
        assertThat(documentIntention1).isEqualTo(documentIntention2);
        documentIntention2.setId(2L);
        assertThat(documentIntention1).isNotEqualTo(documentIntention2);
        documentIntention1.setId(null);
        assertThat(documentIntention1).isNotEqualTo(documentIntention2);
    }
}
