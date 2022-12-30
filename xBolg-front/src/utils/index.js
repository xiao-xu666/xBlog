export const resize = (chart) => {
    let timer = 0;
    window.addEventListener("resize", () => {
      clearTimeout(timer);
  
      timer = setTimeout(() => {
        chart.resize();
      }, 300);
    });
  };