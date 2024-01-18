import React, { useEffect, useState } from 'react';
import Tooltip from '../../components/Tooltip';
import RealtimeChart from '../../charts/RealtimeChart';
import { tailwindConfig, hexToRGB } from '../../utils/Utils';

function DashboardCard01({ dailyRevenue }) {
  // Fonction pour obtenir la date actuelle au format jj-mm-aaaa
  const getCurrentDate = () => {
    const today = new Date();
    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const year = today.getFullYear();
    return `${day}-${month}-${year}`;
  };

  // Fonction pour filtrer et transformer dailyRevenue en labels et data pour le graphique
  const getChartData = (dailyRevenue) => {
    const currentDate = getCurrentDate();
    const filteredEntries = Object.entries(dailyRevenue)
      .filter(([key]) => key.startsWith(currentDate))
      .sort((a, b) => {
        const timeA = a[0].split(' ')[1];
        const timeB = b[0].split(' ')[1];
        return timeA.localeCompare(timeB);
      });

    // Les labels doivent être au format 'DD-MM-YYYY HH:mm:ss' pour correspondre au parser de Chart.js
    const labels = filteredEntries.map(([key]) => key);
    const data = filteredEntries.map(([, value]) => value);

    return {
      labels,
      data,
    };
  };

  const [chartData, setChartData] = useState({
    labels: [],
    datasets: [{
      label: 'Ventes', // Ajouter une étiquette pour la série de données
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
      const newChartData = getChartData(dailyRevenue);
      setChartData({
        ...chartData,
        labels: newChartData.labels,
        datasets: [{
          ...chartData.datasets[0],
          data: newChartData.data,
        }],
      });
    }
  }, [dailyRevenue]);  

  return (
    <div className="flex flex-col col-span-full sm:col-span-6 bg-white dark:bg-slate-800 shadow-lg rounded-sm border border-slate-200 dark:border-slate-700">
      <header className="px-5 py-4 border-b border-slate-200 dark:border-slate-700">
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
