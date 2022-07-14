package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentSource.class);
        DocumentSource documentSource1 = new DocumentSource();
        documentSource1.setId(1L);
        DocumentSource documentSource2 = new DocumentSource();
        documentSource2.setId(documentSource1.getId());
        assertThat(documentSource1).isEqualTo(documentSource2);
        documentSource2.setId(2L);
        assertThat(documentSource1).isNotEqualTo(documentSource2);
        documentSource1.setId(null);
        assertThat(documentSource1).isNotEqualTo(documentSource2);
    }
}
