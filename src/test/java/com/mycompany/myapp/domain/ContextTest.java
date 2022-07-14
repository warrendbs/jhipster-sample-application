package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Context.class);
        Context context1 = new Context();
        context1.setId(1L);
        Context context2 = new Context();
        context2.setId(context1.getId());
        assertThat(context1).isEqualTo(context2);
        context2.setId(2L);
        assertThat(context1).isNotEqualTo(context2);
        context1.setId(null);
        assertThat(context1).isNotEqualTo(context2);
    }
}
