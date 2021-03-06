import { ApiModelProperty } from '@nestjs/swagger';

export class CreateUserDTO {
  @ApiModelProperty()
  password: string;

  @ApiModelProperty()
  firstName: string;

  @ApiModelProperty()
  lastName: string;

  @ApiModelProperty()
  email: string;

  @ApiModelProperty()
  city: string;

  @ApiModelProperty()
  gender: string;
}
