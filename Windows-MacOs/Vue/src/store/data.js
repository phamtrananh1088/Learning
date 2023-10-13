
const data = {
  state: {
    data: {}
  },
  mutations: {
    // setData(state, data) {  //this.$store.commit('setData', data)
    //     state.data = data;
    // },
    clear (state) {
      state.data = {}
    }
  },
  getters: {
    getData: (state) => () => { // store.getters.getData()
      return data
    },
    data: (state) => () => {
      return data
    }
  },
  actions: {
    // setData(context, data) {
    //     context.commit('setData', data); // store.dispatch('push')
    // }
  }
}

export default data
