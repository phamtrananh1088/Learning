import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Animation, AnimationController, IonButton } from '@ionic/angular';

@Component({
  selector: 'app-speaker',
  templateUrl: './speaker.component.html',
  styleUrls: ['./speaker.component.scss'],
})
export class SpeakerComponent  implements OnInit {
  @ViewChild(IonButton, { read: ElementRef }) button?: ElementRef<HTMLIonButtonsElement>;
  @ViewChild('graphA', { static: true }) graphA?: ElementRef;
  @ViewChild('graphB', { static: true }) graphB?: ElementRef;
  private animationButton?: Animation;
  private animationA?: Animation;
  private animationB?: Animation;
  constructor(
    private animationCtrl: AnimationController
  ) { }

  ngOnInit() {}

  ngAfterViewInit() {
    setTimeout(() => {
      this.animationButton = this.animationCtrl
        .create()
        .addElement(this.button!.nativeElement)
        .duration(100)
        .iterations(1)
        .keyframes([
          { offset: 0, top: '5px', height: '100px' },
          { offset: 1, top: '5px',  height: '100px' },
        ]);
      this.animationA = this.animationCtrl
        .create()
        .addElement(this.graphA!.nativeElement)
        .duration(400)
        .iterations(1)
        .keyframes([
          { offset: 0, display: 'none'},
          { offset: 0.5, display: 'block' },
        ])
        .fromTo('transform', 'matrix(1.3600000143051147,0,0,1.3600000143051147,516.219970703125,522.4000244140625) translateX(-5px) scale(0.5,0.5)', 'matrix(1.3600000143051147,0,0,1.3600000143051147,516.219970703125,522.4000244140625) translateX(0px)')
        .fromTo('opacity','0.5','1');
        this.animationB = this.animationCtrl
        .create()
        .addElement(this.graphB!.nativeElement)
        .duration(400)
        .iterations(1)
        // .keyframes([
        //   { offset: 0, transform: 'matrix(1.3600000143051147,0,0,1.3600000143051147,516.219970703125,522.4000244140625) translateX(-5px) scale(0.5,0.5)'},
        //   { offset: 0.5, transform: 'matrix(1.3600000143051147,0,0,1.3600000143051147,516.219970703125,522.4000244140625) translateX(0px)' },
        // ])
        .fromTo('transform', 'matrix(1.3600000143051147,0,0,1.3600000143051147,516.219970703125,522.4000244140625) translateX(-5px) scale(0.5,0.5)', 'matrix(1.3600000143051147,0,0,1.3600000143051147,516.219970703125,522.4000244140625) translateX(0px) ')
        .fromTo('opacity','0','1');
    }, 0);
  }

  click () {
    this.animationButton?.play();
    this.animationA?.play();
    this.animationB?.play();
  }
}
