import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { SpeakerComponent } from './button/speaker/speaker.component';

@NgModule({
  imports: [ CommonModule, FormsModule, IonicModule],
  declarations: [SpeakerComponent],
  exports: [SpeakerComponent]
})
export class ComponentModule {}
