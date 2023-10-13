export default {
    name: "TableExpand",
    functional: true,
    props: {
      render: Function,
      row: {},
      column: {},
      index: { type: Number, default: 0 }
    },
    render: (h, ctx) => {
      const params = {
        row: ctx.props.row, column: ctx.props.column, index: ctx.props.index
      }
      return ctx.props.render(h, params); 
    }
  };
  