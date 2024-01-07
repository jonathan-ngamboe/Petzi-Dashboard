import React, { useEffect, useState, useRef } from 'react';
import Tooltip from '../../components/Tooltip';
import RealtimeChart from '../../charts/RealtimeChart';
import { tailwindConfig, hexToRGB, formatValue } from '../../utils/Utils';

function DashboardCard01({ dailyRevenue }) {
  // Fonction pour transformer dailyRevenue en labels et data pour le graphique
  const getChartData = (dailyRevenue) => {
    const sortedEntries = Object.entries(dailyRevenue).sort((a, b) => {
      return new Date(a[0]) - new Date(b[0]);
    });
  
    const labels = sortedEntries.map(([key]) => key);
    const data = sortedEntries.map(([, value]) => value);
  
    return {
      labels,
      data,
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
  
  const customDateStringToISO = (customDateStr) => {
    const [datePart, timePart] = customDateStr.split(' ');
    const [day, month, year] = datePart.split('-');
    return `${year}-${month}-${day}T${timePart}`;
  };

  const filterLatestDateData = (revenueData) => {
    const isoDates = Object.keys(revenueData).map(customDateStringToISO);
    const latestDate = new Date(Math.max.apply(null, isoDates.map(isoDate => new Date(isoDate))));
    const latestDateString = `${("0" + latestDate.getDate()).slice(-2)}-${("0" + (latestDate.getMonth() + 1)).slice(-2)}-${latestDate.getFullYear()}`;

    const latestData = Object.entries(revenueData).reduce((acc, [date, value]) => {
      const [dateStr] = date.split(' ');
      if (dateStr === latestDateString) {
        acc[date] = value;
      }
      return acc;
    }, {});

    return latestData;
  };

  useEffect(() => {
    if (dailyRevenue) {
      // Filtre pour obtenir les données de la dernière date
      const latestData = filterLatestDateData(dailyRevenue);
      const { labels, data } = getChartData(latestData);
      setChartData({
        labels,
        datasets: [{ ...chartData.datasets[0], data }]
      });
    }
  }, [dailyRevenue]);

  return (
    <div className="flex flex-col col-span-full sm:col-span-6 bg-white dark:bg-slate-800 shadow-lg rounded-sm border border-slate-200 dark:border-slate-700">
      <header className="px-5 py-4 border-b flex items-center">
        <h2 className="font-semibold">Montant des ventes en temps réel</h2>
        <Tooltip className="ml-2">
          <div className="text-xs text-center">Ventes réalisées aujourd'hui en temps réel</div>
        </Tooltip>
      </header>
      <RealtimeChart data={chartData} width={595} height={248} />
    </div>
  );
}

export default DashboardCard01;
