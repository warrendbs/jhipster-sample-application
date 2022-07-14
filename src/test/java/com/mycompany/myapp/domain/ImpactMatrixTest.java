package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImpactMatrixTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImpactMatrix.class);
        ImpactMatrix impactMatrix1 = new ImpactMatrix();
        impactMatrix1.setId(1L);
        ImpactMatrix impactMatrix2 = new ImpactMatrix();
        impactMatrix2.setId(impactMatrix1.getId());
        assertThat(impactMatrix1).isEqualTo(impactMatrix2);
        impactMatrix2.setId(2L);
        assertThat(impactMatrix1).isNotEqualTo(impactMatrix2);
        impactMatrix1.setId(null);
        assertThat(impactMatrix1).isNotEqualTo(impactMatrix2);
    }
}
