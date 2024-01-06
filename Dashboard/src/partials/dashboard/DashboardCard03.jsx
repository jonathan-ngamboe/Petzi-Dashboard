import React from 'react';

function DashboardCard03({ eventRevenue, eventSalesCount }) {
  return (
    <div className="col-span-full xl:col-span-12 bg-white dark:bg-slate-800 shadow-lg rounded-sm border border-slate-200 dark:border-slate-700">
      <header className="px-5 py-4 border-b border-slate-100 dark:border-slate-700">
        <h2 className="font-semibold text-slate-800 dark:text-slate-100">Évènements</h2>
      </header>
      <div className="p-3">
        {/* Table */}
        <div className="overflow-x-auto">
          <table className="table-auto w-full dark:text-slate-300">
          {/* Table header */}
          <thead className="text-xs uppercase text-slate-400 dark:text-slate-500 bg-slate-50 dark:bg-slate-700 dark:bg-opacity-50 rounded-sm">
                <tr>
                <th className="p-2">
                  <div className="font-semibold text-left">Nom</div>
                </th>
                <th className="p-2">
                  <div className="font-semibold text-center">Ventes</div>
                </th>
                <th className="p-2">
                  <div className="font-semibold text-center">Revenus</div>
                </th>
              </tr>
            </thead>            
            <tbody className="text-sm font-medium divide-y divide-slate-100 dark:divide-slate-700">
              {/* Générer dynamiquement les lignes du tableau */}
              {Object.keys(eventRevenue).map((eventName, index) => (
                <tr key={index}>
                  <td className="p-2">
                    <div className="flex items-center">
                      {/* ...SVG et nom de l'événement... */}
                      <div className="text-slate-800 dark:text-slate-100">{eventName}</div>
                    </div>
                  </td>
                  <td className="p-2">
                    <div className="text-center">{eventSalesCount[eventName]}</div>
                  </td>
                  <td className="p-2">
                    <div className="text-center text-emerald-500">CHF {eventRevenue[eventName]}</div>
                  </td>
                  {/* Autres colonnes si nécessaire */}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default DashboardCard03;
