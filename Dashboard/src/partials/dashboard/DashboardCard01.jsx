import React, { useEffect, useState, useRef } from 'react';
import Tooltip from '../../components/Tooltip';
import RealtimeChart from '../../charts/RealtimeChart';
import { tailwindConfig, hexToRGB, formatValue } from '../../utils/Utils';

function DashboardCard01({ dailyRevenue }) {
  const totalSalesRef = useRef(); // Référence pour le montant total des ventes
  const averageSalesRef = useRef(); // Référence pour la moyenne des ventes

  // Fonction pour transformer dailyRevenue en labels et data pour le graphique
  const getChartData = (dailyRevenue) => {
    const sortedEntries = Object.entries(dailyRevenue).sort((a, b) => {
      return new Date(a[0]) - new Date(b[0]);
    });
  
    const labels = sortedEntries.map(([key]) => key);
    const data = sortedEntries.map(([, value]) => value);
    const totalSales = data.reduce((sum, value) => sum + value, 0);
    const averageSales = totalSales / data.length;
  
    if (totalSalesRef.current) {
      totalSalesRef.current.textContent = formatValue(totalSales);
    }
  
    if (averageSalesRef.current) {
      averageSalesRef.current.textContent = formatValue(averageSales);
    }
  
    return {
      labels,
      data,
      totalSales,
      averageSales
    };
  };  

  const [chartData, setChartData] = useState({
    labels: [],
    datasets: [{
      data: [],
      fill: true,
      backgroundColor: `rgba(${hexToRGB(tailwindConfig().theme.colors.blue[500])}, 0.08)`,
      borderColor: tailwindConfig().theme.colors.indigo[500],
      borderWidth: 2,
      tension: 0,
      pointRadius: 0,
      pointHoverRadius: 3,
      pointBackgroundColor: tailwindConfig().theme.colors.indigo[500],
      pointHoverBackgroundColor: tailwindConfig().theme.colors.indigo[500],
      pointBorderWidth: 0,
      pointHoverBorderWidth: 0,
      clip: 20,
    }],
  });

  useEffect(() => {
    if (dailyRevenue) {
      const { labels, data } = getChartData(dailyRevenue);
      setChartData({
        labels,
        datasets: [{ ...chartData.datasets[0], data }]
      });
    }
  }, [dailyRevenue]);  

  return (
    <div className="flex flex-col col-span-full sm:col-span-6 bg-white shadow-lg rounded-sm border">
      <header className="px-5 py-4 border-b flex items-center">
        <h2 className="font-semibold">Montant des ventes en temps réel</h2>
        <Tooltip className="ml-2">
          <div className="text-xs text-center">Ventes réalisées aujourd'hui en temps réel</div>
        </Tooltip>
      </header>
      <div className="px-5 py-3">
        {/* Valeur totale des ventes */}
        <div className="flex justify-between">
          <div className="text-3xl font-bold text-slate-800 dark:text-slate-100 mr-2 tabular-nums">
            Total : <span ref={totalSalesRef}></span>
          </div>
          {/* Valeur moyenne des ventes */}
          <div className="text-3xl font-bold text-slate-800 dark:text-slate-100 mr-2 tabular-nums">
            Moyenne : <span ref={averageSalesRef}></span>
          </div>
        </div>
      </div>
      <RealtimeChart data={chartData} width={595} height={248} />
    </div>
  );
}

export default DashboardCard01;
