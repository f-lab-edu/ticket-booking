package org.project.domain.user.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@JsonTest
@MockBean(JpaMetamodelMappingContext.class)
public class GoogleMemberInfoResponseTest {

  @Autowired
  private JacksonTester<GoogleUserInfoResponse> json;

  @Test
  public void testDeserialize() throws Exception {
    String email = "test@test.com";
    String familyName = "test family name";
    String gender = "test gender";
    String givenName = "test given name";
    String hd = "test hd";
    String id = "test id";
    String link = "test link";
    String locale = "test locale";
    String name = "test name";
    String picture = "test picture";
    boolean verifiedEmail = true;
    String content = "{\"email\":\"" + email + "\",\"family_name\":\"" + familyName
        + "\",\"gender\":\"" + gender + "\",\"given_name\":\"" + givenName + "\",\"hd\":\"" + hd
        + "\",\"id\":\"" + id + "\",\"link\":\"" + link + "\",\"locale\":\"" + locale
        + "\",\"name\":\"" + name + "\",\"picture\":\"" + picture + "\",\"verified_email\":"
        + verifiedEmail + "}";

    GoogleUserInfoResponse response = json.parseObject(content);

    assertThat(response.getEmail()).isEqualTo(email);
    assertThat(response.getFamilyName()).isEqualTo(familyName);
    assertThat(response.getGender()).isEqualTo(gender);
    assertThat(response.getGivenName()).isEqualTo(givenName);
    assertThat(response.getHd()).isEqualTo(hd);
    assertThat(response.getId()).isEqualTo(id);
    assertThat(response.getLink()).isEqualTo(link);
    assertThat(response.getLocale()).isEqualTo(locale);
    assertThat(response.getName()).isEqualTo(name);
    assertThat(response.getPicture()).isEqualTo(picture);
    assertThat(response.isVerifiedEmail()).isEqualTo(verifiedEmail);
  }


}
