import {
  Body,
  Controller,
  Delete,
  Get,
  Param,
  Post,
  Put,
  UseGuards,
} from '@nestjs/common';
import { AuthGuard } from '@nestjs/passport';

import { ProductService } from './product.service';
import { Product } from './../../types/product';
import { FilterDTO } from './../../dto/filter.dto';

@Controller('product')
export class ProductController {
  constructor(private productService: ProductService) {}

  @Post()
  async listAll(@Body() filters: FilterDTO): Promise<object> {
    if (!filters) {
      return await this.productService.findAll();
    } else {
      // console.log(filters.major)
      return await this.productService.filterProducts(filters);
    }
  }

  // @Get('/mine')
  // @UseGuards(AuthGuard('jwt'), SellerGuard)
  // async listMine(@User() user: UserDocument): Promise<Product[]> {
  //   const { id } = user;
  //   return await this.productService.findByOwner(id);
  // }

  // @Get('/seller/:id')
  // async listBySeller(@Param('id') id: string): Promise<Product[]> {
  //   return await this.productService.findByOwner(id);
  // }

  // @Post()
  // @UseGuards(AuthGuard('jwt'), SellerGuard)
  // async create(
  //   @Body() product: CreateProductDTO,
  //   @User() user: UserDocument,
  // ): Promise<Product> {
  //   return await this.productService.create(product, user);
  // }

  @Get(':id')
  async read(@Param('id') id: string): Promise<Product> {
    return await this.productService.findById(id);
  }

  // @Put(':id')
  // @UseGuards(AuthGuard('jwt'), SellerGuard)
  // async update(
  //   @Param('id') id: string,
  //   @Body() product: UpdateProductDTO,
  //   @User() user: UserDocument,
  // ): Promise<Product> {
  //   const { id: userId } = user;
  //   return await this.productService.update(id, product, userId);
  // }

  // @Delete(':id')
  // @UseGuards(AuthGuard('jwt'), SellerGuard)
  // async delete(
  //   @Param('id') id: string,
  //   @User() user: UserDocument,
  // ): Promise<Product> {
  //   const { id: userId } = user;
  //   return await this.productService.delete(id, userId);
  // }
}
