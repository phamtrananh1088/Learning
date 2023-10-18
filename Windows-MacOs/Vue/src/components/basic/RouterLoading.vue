<template>
  <div class="router-loading" style="background: #eeeeee5c;" >
    <div :class="['loader-' + theme]">Loading...</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      theme: "green",
    };
  },
  
  created() {
    let theme = this.$store.getters.getMainTheme();
    if (theme) {
      this.theme = theme;
    }
  }
};
</script>
<style scoped>
.router-loading {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  font-size: 100px;
  text-align: center;
  padding-top: 200px;
  z-index: 9999;
}
.loader-green,
.loader-green:after {
  border-radius: 50%;
  width: 10em;
  height: 10em;
}
.loader-green {
  margin: 60px auto;
  font-size: 10px;
  position: relative;
  text-indent: -9999em;
  border-top: 1.1em solid rgba(146, 208, 80, 0.2);
  border-right: 1.1em solid rgba(146, 208, 80, 0.2);
  border-bottom: 1.1em solid rgba(146, 208, 80, 0.2);
  border-left: 1.1em solid #ffffff;
  -webkit-transform: translateZ(0);
  -ms-transform: translateZ(0);
  transform: translateZ(0);
  -webkit-animation: load8 1.1s infinite linear;
  animation: load8 1.1s infinite linear;
}

.loader-blue,
.loader-blue:after {
  border-radius: 50%;
  width: 10em;
  height: 10em;
}
.loader-blue {
  margin: 60px auto;
  font-size: 10px;
  position: relative;
  text-indent: -9999em;
  border-top: 1.1em solid rgba(59, 73, 159, 0.2);
  border-right: 1.1em solid rgba(59, 73, 159, 0.2);
  border-bottom: 1.1em solid rgba(59, 73, 159, 0.2);
  border-left: 1.1em solid #ffffff;
  -webkit-transform: translateZ(0);
  -ms-transform: translateZ(0);
  transform: translateZ(0);
  -webkit-animation: load8 1.1s infinite linear;
  animation: load8 1.1s infinite linear;
}

.loader-orange,
.loader-orange:after {
  border-radius: 50%;
  width: 10em;
  height: 10em;
}
.loader-orange {
  margin: 60px auto;
  font-size: 10px;
  position: relative;
  text-indent: -9999em;
  border-top: 1.1em solid rgba(255, 152, 0, 0.2);
  border-right: 1.1em solid rgba(255, 152, 0, 0.2);
  border-bottom: 1.1em solid rgba(255, 152, 0, 0.2);
  border-left: 1.1em solid #ffffff;
  -webkit-transform: translateZ(0);
  -ms-transform: translateZ(0);
  transform: translateZ(0);
  -webkit-animation: load8 1.1s infinite linear;
  animation: load8 1.1s infinite linear;
}
@-webkit-keyframes load8 {
  0% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes load8 {
  0% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}

</style>
