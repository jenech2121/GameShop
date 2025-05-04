import { reactive, computed } from 'vue';

export const useCalc = () => {
    interface SelectedData {
        motherboard?: Part;
        cpu?: Part;
        gpu?: Part;
        ram?: Part;
        power?: Part;
        cooler?: Part;
        case?: Part;
    }

    const selectedData = reactive<SelectedData>({
        motherboard: undefined,
        cpu: undefined,
        gpu: undefined,
        ram: undefined,
        power: undefined,
        cooler: undefined,
        case: undefined,
    });

    const addSelectedData = (part: Part, key: keyof SelectedData) => {
        selectedData[key] = part; // Присваиваем новый объект
    };

    const removeSelectedData = (key: keyof SelectedData) => {
        if (selectedData[key]) {
            selectedData[key] = undefined; // Устанавливаем значение в undefined
        }
    };

    

    interface Part {
        id: string;
        name: string;
        price: number;
        image: string;
    }

    const partsMotherboards = reactive<Part[]>([
        {
            id: 'asus_01',
            name: 'Asus Prime A520M-A',
            price: 1000,
            image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUcMzmoQ2ysXJrw-W6Hk9_VGJlfdFXk_Ye4A&s',
        },
        {
            id: 'asus_02',
            name: 'Asus Prime B560M-A',
            price: 1200,
            image: 'https://www.tiburcc.it/img/prodimg_l/ea2b8b82b23608897103573c4458ca4d-hi.jpg',
        },
    ]);

    const partsCpu = reactive<Part[]>([
        {
            id: 'amd_01',
            name: 'AMD Ryzen 5 5600X',
            price: 2500,
            image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiblkEM2gCJ8hizpXVUwGn7mvukmKVV1CABQ&s',
        },
        {
            id: 'intel_01',
            name: 'Intel Core i5-12400F',
            price: 3000,
            image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQkwHI2Ii5730zSfaLgPlkg-OGhjku-Z43H6w&s',
        },
    ]);

    const partsGpu = reactive<Part[]>([
        {
            id: 'nvidia_01',
            name: 'NVIDIA GeForce RTX 3060',
            price: 5000,
            image: 'https://dlcdnwebimgs.asus.com/files/media/80edfc49-cc69-47e1-afe6-992488abbb96/img/kv/product-kv_s.jpg',
        },
        {
            id: 'amd_02',
            name: 'AMD Radeon RX 6700 XT',
            price: 6000,
            image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s',
        },
    ]);
    const partsRam = reactive<Part[]>([
        {
            id: '3',
            name: 'NVIDIAecce GeForce RTX 3060',
            price: 5000,
            image: 'https://dlcdnwebimgs.asus.com/files/media/80edfc49-cc69-47e1-afe6-992488abbb96/img/kv/product-kv_s.jpg',
        },
        {
            id: 'amd_ecec02',
            name: 'AMD Rececadeon RX 6700 XT',
            price: 6000,
            image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s',
        },
    ]);

    

    // Вычисляемый список всех выбранных товаров
    const getSelectedItems = computed(() => {
        return Object.values(selectedData).filter((item): item is Part => Boolean(item));
    });

    const products = computed(() => [
        ...partsMotherboards,
        ...partsCpu,
        ...partsGpu,
        ...partsRam,
    ]);
    const getKeyByItemId = (itemId: string): keyof typeof selectedData | undefined => {
        for (const [key, value] of Object.entries(selectedData)) {
            if (value && value.id === itemId) {
                return key as keyof typeof selectedData;
            }
        }
        return undefined;
    };

    return {
        selectedData,
        addSelectedData,
        removeSelectedData,
        partsMotherboards,
        partsCpu,
        partsGpu,
        partsRam,
        products,
        getSelectedItems,
        getKeyByItemId 
    };
};