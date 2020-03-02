package kr.co.inslab.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserInvitation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-26T15:17:27.527+09:00[Asia/Seoul]")
public class UserInvitation   {
  @JsonProperty("eamil")
  private String eamil = null;

  public UserInvitation eamil(String eamil) {
    this.eamil = eamil;
    return this;
  }

  /**
   * Get eamil
   * @return eamil
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getEamil() {
    return eamil;
  }

  public void setEamil(String eamil) {
    this.eamil = eamil;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInvitation userInvitation = (UserInvitation) o;
    return Objects.equals(this.eamil, userInvitation.eamil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eamil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInvitation {\n");
    
    sb.append("    eamil: ").append(toIndentedString(eamil)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
