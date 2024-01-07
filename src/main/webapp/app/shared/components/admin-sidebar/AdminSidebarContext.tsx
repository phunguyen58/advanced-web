import React, { createContext, useState, useMemo, ReactNode } from 'react';

interface MenuContextProps {
  activeMenu: string;
  setActiveMenu: React.Dispatch<React.SetStateAction<string>>;
}

interface MenuProviderProps {
  children: ReactNode;
}

export const MenuContext = createContext<MenuContextProps>({
  activeMenu: 'user',
  setActiveMenu() {},
});

export const MenuProvider: React.FC<MenuProviderProps> = ({ children }) => {
  const [activeMenu, setActiveMenu] = useState<string>('');

  const value = useMemo(() => {
    return {
      activeMenu,
      setActiveMenu,
    };
  }, [activeMenu]);

  return <MenuContext.Provider value={value}>{children}</MenuContext.Provider>;
};
