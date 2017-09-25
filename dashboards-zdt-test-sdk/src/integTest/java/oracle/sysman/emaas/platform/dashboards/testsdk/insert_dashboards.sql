DECLARE
  V_DASHBOARD_ID NUMBER(38,0);
  V_INDEX     NUMBER(38,0);
  V_TILE      NUMBER(38,0);
  V_PARAM_VALUE_CLOB_SCREENSHOT CLOB;
BEGIN
  V_INDEX := 0;
  V_TILE := 8000000;
  V_DASHBOARD_ID :=2000000;
  V_PARAM_VALUE_CLOB_SCREENSHOT      :=''data:image/png;base64,''||
  ''iVBORw0KGgoAAAANSUhEUgAAALYAAABtCAYAAAAMCZvoAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAA08SURBVHhe7Z3bT1vZFcaXb2AMBgKGODEDUYCkmShTJVMpLzMPkZKHomge2mqeR33tP1DNw6Qvo0rzUKlSpaoXqdGoijSVKlWqmplWQuoMGpI0MEwu5EICmIQ4xJhLIGBj+9jd3/ZxhhDbZNh7h4NZv+jknLOPvWzs7yyvvde+uPICYpgqw23vHY3pe4/tV2Yn2lcWdjabpUQiYZ/pB3901srZZ2bIWpZ9ZIZMlu1XwoR9ZWHjXltbWyucGMDlconNrMeQfwSzbeQMeGzlGNsSHjv25AmFwhHK50x5VrxFV+HQCGbtuygnXsFc1GfaPrlyFPB67RP9pDJZ8vv02lcWdt7K0m8HJujzhJ8avXntzg9y021zPUX7pl5H2hWGxQ/PjrMP2xkrT82Nfjp/JlwoNIAjhU05i375+Rj9YdxNIT8+Cr3gzckvz97rhu1XJpWxqL2tgYbff8Mu0Y9jhX3uP/foL1NeCtUWPmCmelgTwm5tbaCBn0TsEv2YEHbJwGxlZYXGx8fp7t27NDY2Rul0mu7cuUPJZJIePXokK4uPHz+mqWhUWHAVxCz+Y1FXH4pub9soKexnz55RJpOhrq4uWlxclMcPHz6kubk5mp+flwJ/IiqMT+JPxKMNVloYZouUDEVyuZxsn8al5eVl8VPUSqlUSl6rqamR5S''||
  ''AlBN7QUE8f/XuMzke91OqXxUwVkUpbFArtvFBES4z9EWLsqIdaDcXYxcqRKdh+aVziHyqPrULYX+8+YWfpwy/u0Z8nbWEb+QZh1ESdvwjbLwUsQth72xvo8s92WatIToQsI9HHlA22kdeMqmVo5Habi+XN27eEfY99ph+T9pEVdAuFvx0O2CX6caSwM0LYC4k4tYf32yUmQEbTZCWV7VckbwmlmLsxX1tz3/clYyqTLsBtt5Y1+AKCtbRZ+6mdbj9j5pfYJCbdyM7BZPgL2P5rZ4cIewd+ssy2okXYeV+9fWQAaNprLr5jqhMNlcc8zV35HYWffiEEGCy0PGkE5vJC3O6iXStF1PUe0dGf2wXqrInKS63myst6TFSO1sP2X0bZY7vQFpRbE+qYJ0ovCaU/1bq5xOZOF8+F/bU5Ie4V+9UZpjR6YmyXMOP2iU3cdXJvahP2XWKP12OYCpRVCPqKzMzM0LVr12h1dZXu3btH8XicFhYWaHZ2Vh5Ho1GpMa7aMU6jrLC9Xq/s2TcwMCAzcxD57du3KRaL0fT0tByLiLKCAZY24yyUK4/InSQGf0PhuX8Q+ZrtUoOknxF1/5To2C/sAnW48liZXVl5LACPjQ3miseGNgzuk8cMUx4tHnvuv7+ivbHzRDUtokRze58EQoZdsYfHPvIB0dsfyis6YI9dmZ1oX70d27IoPnWdIk0WUQ6JFL3CzuddZOUs8nps21aWKLCXqLGr8AANsLArszuFnc1SfHaeIvva7RK94M2hj0+tpqCpFCzsyuxE+3rk''||
  ''khde1BRQds6gfaYqMegHGWb7YGEzVYkeYXPrG+Mw1IUt6p5rQzHKTi7aBQyz/SgLG0P0c8spyiXTdgnDbD9aQhGXR5hB91WGcQhlhY35+zClGebvwxRno6Ojcs6+69evy3PM4TcZjRqdtoBhtkpFVULUmMcPgsZ+YmKCgsGg3F+6dEnO34deflx7ZJzGpplHeG3M3ff06VNqaGiQXVYxWSVYWl6m+roARf/+P3rjhz1U84M2Wa4TvLu0laVagzPqc+axMlWZeYSoQVNTE3k8nueiBo3Ceytm5BnGCFoC5DxW3bJY4IxzUBY25FxzqI18kaZCAcM4AHWPLeqNns4mcjXX2gUMox+0xBUpHmNcLkDDxka0hCJytAHDGGJwcFAOJkdr3MjICA0PD8uWOpRhSRmstrG0tGQ/uoAeYTOMZtJWTmyFeltvb48cUF5fX0+dnZ0ydwIh19bWUiQSkeskNTY2yscW0TPQIB6nyH4z0whzc9/mVKP9A7+/I4e3Dn9wiIKuNLk8XiloyNWyLPL5fDIUQRla6zai7LGRmvHKJA3D6OPcu/vp3Dv7qdnvptFbt2TmGyHHLXF85coVuZLdgwcPqL+/X+ZYNqLssZPiBUZnZuhH4ifCBOyxN6cq7Wfihb2vXQoXq9Uhl4L5bjCfDZKFqETCY4fDYenB16Ms7FUh7OFYjN49cMAu0QsLe3Oq0v6/3oM6ic78lfK+JhkZQKqFLhwvHoON51o8NoT9Dgu7LCzsypS0P32xEOdG+uizz/5Gb711TIYeoVBI9lHq7u''||
  ''6WFUh4cLSW9PX1UV1dXeG5grLCxvqOcPVY3xHG0Juvo6ODEokE7dmzRxrF6wZbWmhoepqFXQEWdmVK2U+OLsrpowNvNtOdu3epzu+XlURsqDSihQRNfjiHNo8fP24/s0DZyiOecPPmTdlNFYYwbx96+01OTsrlphHIfzMywt1WGSPM/ulLSvzxS8qtZKj3UC+1CAcKx7pv3z7ZwxQb4uy2tjY6evSo/azvqBiK4BI8tl/cLRA3xI67BOeIZzIiDMmI/TfijmGPXR722JUpZT8/tSgiAvHld+2hrwa+osZgo1wVGhVJtIicPHny+eSocLpnz561n1mAY2wBC7sy22F/LF1Y4ry3xk+WCIkTc3MyCYOkDCIGAKEjLIbTXR9fA+U4AndFVu3eYJiX+GQiSp9MTlFSHCdmZ2WUAB+MfiFo2kPGEWJGBIEE4Ub/rCxsj/gpiGxoQ2QYVT7uPkAfHzxAWA84NjMj63uYmx0tIEjQQOAIQTCHO/qPFL14EeVQBCn1WXHH7OeUelk4FKlMKfv/fIhQxE0/jtSQ15Wn1WRShh5o3oOoEZIgBIEnRwvexr4iWpo0OBBhdPPpRIo+nUzSqpUT7tdFgUBAihqgNQThCEIRVB43ihpwWx3jSH59vEFs9dTg/S6b+H1gYTOOpKfRSz1BL7mFR94KysLGy3q29toMYwxlYadzeZpa5SibcRbKwha6pgWeto9xGOqhiAhDMHUfwziJV5IkxpuhIRw5eswMlUwm5Wq98+LYxcs/Mw7klVSJZaax9PSNGzfk0Bxk''||
  ''gbBhtLBbuGuuOzJO45Uyj+iXjWzP+oeiYdzKZinQ3EJfT8zQmcMR+4peOPO4OWz/ZV7JYyPLg15U6BNb3HAeamsTwuM5RRjnoRwgw6Mi68kwTkJZ2EjOtPLsZozDUBa2z+2irjquPjLOQj0UEVtm0+onw7xelIXNME6Ehc1UJcrCRnTNvfsYp6Es7KyIr2cKA4oZxjEoCxu9+5Lcjs04DC0xNgfqjNMoq0mMAEaPvqmpKTl3A4a3YzQwevWhUxSuYXS6m5eaZhxIWWFjRDCmkLp48aLsror1Popz+WGaKQj8/v37hTFprG3GYVTs3YclEdCzD3M44BgPLS6TgHn8LCtLaRFfX38Qp5PdPK9IObj3XWVee+8+iLc4ASU8OASNc5QXrnul8BjGaWip9/GivIzTUBY2kjPNNfYJwzgEPcI2F34xzJZQFjaiEI5EGKehJcZmGKfBwmaqEhY2U5WoCxsJlBynHhlnoSxsS2yrLGzGYSgLG5JmWTNO4wVho6MTQM89rO+IhWwwAxT6i6CfCBZnByjDtjA/L9PtDOM0XhA2xIv1qvv7+2XPvvHxcbp69arco2ff5cuX6cKFC/Jxg4ODNPLtt+RFvxEWN+MwXujdh0knjx07JmdUxQpN6JqKjk84Lq7OhCWmMfsqnoa5+5paQnQ/Fqc3O8O2Fb3g3XHvvsqw/ZdRXg4Pohh/nGBhV4CFVxkT9pUrj0p3BcMYQlnYMFDjYnkzzkJd2KLeGPDscGGbfvtsvzIG7CsLGyNr3PaIGhOgwcVkfA1qa8za9x''||
  ''u2v9Pfvwn7ypXHoeFhqq+vp3wuJyeEhzlFkxK0xmBkPJYTfraySlYmLSeaR6uMDvu4IWdnZylQV0crqRQ+CLmUMVqAdNjH8DnkAzweN62sJCmbzVAoFNJqH4OqC8c+isdnKBwOy2WY0WqlCuwjV4HZCpLJNQoE/NJuU1OTvK76NyD/gdkPsJ4R5qbxip/+evH5Y01+fMeqKAt7YGCA2tvbxZe3IpsKIUhdoO0cAsxmMnRzdJT6+vpKrpu9VZCQQvIpFnskRJ6g06dPU5u4eXSBnACmqoA40JQK+x0dHfZVdSA03JxIpmHGAIjiyJEj9lU9ICkH+0NDQ3Tw4EE6ceKEfUUP8XhcfP4xmTeBgzx16pR9RQ1lYeOOgxcCxUG+usAdDdvYox1dN5hWAjcOvEcxg4qPQ1c2FcIrtvvr8ELlwM0DT2rqtSDs4udvwj5ufHwPALMg6NCRsrB3MwiV4PHh5fHl4wvHOcIBhAmYtgLgywoGg7S0tEStra12iFIIVZqbm+Xj8LMPj4XnmrwJdgssbAUQakCc2CNEglgRkkGYvb298hy/aMjkwqv29PQ8/0nH4yF0CBldFjo7O6XXOnz4sJFfp90GC1sBeOVAICCPi3ULeF5UQiFoCBzl6JqAsiIQNDw4Qix8/MUKX3EyIkYdFjZTlXAwx1QlLGymCiH6P2jQwNx5eK8bAAAAAElFTkSuQmCC'';

  LOOP
  insert into ems_dashboard values(V_DASHBOARD_ID,''Test dashboard'',0,CONCAT(''Test dashboard description'',V_DASHBOARD_ID),''18-JUL-17 05.09.38.583000000 AM'',''18-JUL-17 05.09.38.583000000 AM'',''Oracle'',''Oracle'',0,3,1,V_PARAM_VALUE_CLOB_SCREENSHOT,0,673850131,1,0,1,0,null,1);

    
  insert into ems_dashboard_tile values(V_DASHBOARD_ID,''18-JUL-17 05.09.38.583000000 AM'',''18-JUL-17 05.09.38.583000000 AM'',null,''Oracle'', ''Test Tile'', 2,12,0,2,673850131,2005,''Test Widget'',''Widget description'',''Log Analytics'',null, ''js/../emcsDependencies/dfcommon/images/sample-widget-histogram.png'',''ORACLE'', ''18-JUL-17 05.09.38.583000000 AM'',1,''emcla-visualization'',''/js/viewmodel/search/widget/VisualizationWidget.js'',''/html/search/widgets/visualizationWidget.html'',''LogAnalyticsUI'',''1.0'',''assetRoot'',0,0,0,1,null,0,null,V_TILE,0);
	
	insert into ems_dashboard_tile values(V_DASHBOARD_ID,''18-JUL-17 05.09.38.583000000 AM'',''18-JUL-17 05.09.38.583000000 AM'',null,''Oracle'', ''Test Tile'', 2,12,0,2,673850131,2005,''Test Widget'',''Widget description'',''Log Analytics'',null, ''js/../emcsDependencies/dfcommon/images/sample-widget-histogram.png'',''ORACLE'', ''18-JUL-17 05.09.38.583000000 AM'',1,''emcla-visualization'',''/js/viewmodel/search/widget/VisualizationWidget.js'',''/html/search/widgets/visualizationWidget.html'',''LogAnalyticsUI'',''1.0'',''assetRoot'',2,0,0,1,null,0,null,V_TILE+1,0);
	
	insert into ems_dashboard_tile values(V_DASHBOARD_ID,''18-JUL-17 05.09.38.583000000 AM'',''18-JUL-17 05.09.38.583000000 AM'',null,''Oracle'', ''Test Tile'', 2,12,0,2,673850131,2005,''Test Widget'',''Widget description'',''Log Analytics'',null, ''js/../emcsDependencies/dfcommon/images/sample-widget-histogram.png'',''ORACLE'', ''18-JUL-17 05.09.38.583000000 AM'',1,''emcla-visualization'',''/js/viewmodel/search/widget/VisualizationWidget.js'',''/html/search/widgets/visualizationWidget.html'',''LogAnalyticsUI'',''1.0'',''assetRoot'',4,0,0,1,null,0,null,V_TILE+2,0);
	
	insert into ems_dashboard_tile values(V_DASHBOARD_ID,''18-JUL-17 05.09.38.583000000 AM'',''18-JUL-17 05.09.38.583000000 AM'',null,''Oracle'', ''Test Tile'', 2,12,0,2,673850131,2005,''Test Widget'',''Widget description'',''Log Analytics'',null, ''js/../emcsDependencies/dfcommon/images/sample-widget-histogram.png'',''ORACLE'', ''18-JUL-17 05.09.38.583000000 AM'',1,''emcla-visualization'',''/js/viewmodel/search/widget/VisualizationWidget.js'',''/html/search/widgets/visualizationWidget.html'',''LogAnalyticsUI'',''1.0'',''assetRoot'',6,0,0,1,null,0,null,V_TILE+3,0);

  
    DBMS_OUTPUT.PUT_LINE(''Search with ID ''||V_DASHBOARD_ID||'' has been created!'');
    
    V_INDEX := V_INDEX + 1;
    V_TILE := V_TILE+5;
    V_DASHBOARD_ID := V_DASHBOARD_ID + 1;
    IF (V_INDEX>=50) THEN
      EXIT;
    END IF;
  END LOOP;
    COMMIT;
EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE(''FAILED TO  ADD  Security Analytics OOB WIDGET DUE TO ''||SQLERRM);
  RAISE;
END;
/