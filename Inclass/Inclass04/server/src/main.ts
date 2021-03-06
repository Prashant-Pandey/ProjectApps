import { NestFactory } from '@nestjs/core';
import { ExpressAdapter } from '@nestjs/platform-express';
import 'dotenv/config';
import * as Express from 'express';
import * as cors from 'cors';

import { AppModule } from './app.module';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';

if (process.env.NODE_ENV === 'test') {
  process.env.MONGO_URI = process.env.MONGO_URI_TEST;
  // console.log('----------TESTING IN PROCESS----------');
  // console.log('using database', process.env.MONGO_URI);
}

const server = Express();
server.use(cors());
server.get('/', (req, res) => res.send('ok server running'));
// server.get('/_ah/health', (req, res) => res.send('ok'));
// server.get('/_ah/start', (req, res) => res.send('ok'));

async function bootstrap() {
  // console.log('using database', process.env.MONGO_URI);
  const app = await NestFactory.create(AppModule, new ExpressAdapter(server));

  // global prefix
  app.setGlobalPrefix('api');

  // swagger
  const document = SwaggerModule.createDocument(
    app,
    new DocumentBuilder().setBasePath('api').build(),
  );
  SwaggerModule.setup('docs', app, document);

  await app.listen(process.env.PORT);
}
bootstrap();
